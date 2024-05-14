package com.bewd.aspects;

import com.bewd.annotations.Page;
import com.bewd.common.PagingDTO;
import com.bewd.common.PagingParam;
import com.bewd.common.ResultData;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Date 2019/4/25 13:45
 * @Created by Efy
 * @Description TODO
 */
@Aspect
@Component
public class PageAspect {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.bewd.annotations.Page)")
    private void pageAspect() {
        log.debug("注册分页切面.");
    }

    @Around("pageAspect()")
    private ResultData around(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();
        Method method = getCurrMethod(methodName,point.getTarget());
        Page page = method.getAnnotation(Page.class);
        boolean export = page.export();
        PagingParam paParam = null;
        for(Object param : point.getArgs()){
            if(param instanceof PagingParam){
                paParam = (PagingParam) param;
            }
        }
        if(paParam != null){
            prePageParam(paParam,export);
        }
        ResultData result = (ResultData) point.proceed();
        if(result.getData() instanceof List){
            if(!page.rawData()){
                result.setData(prePageResult((List)result.getData()));
            }
        }
        return result;
    }

    /**
     * 分页参数前置处理逻辑
     *
     * @param param
     * @param <T>
     */
    public <T extends PagingParam> void prePageParam(T param,boolean export) {
        if(export){
            PageHelper.startPage(1, Integer.MAX_VALUE - 1);
        }else{
            PageHelper.startPage(param.getPage(), param.getRows());
        }
        if (param.getSord() != null && !"".equals(param.getSord())) {
            PageHelper.orderBy(param.getSidx() + " " + param.getSord());
        }
    }

    /**
     * 分页结果前置处理逻辑
     *
     * @param result
     * @param <T>
     * @return
     */
    public <T> PagingDTO<T> prePageResult(List<T> result) {
        PageInfo<T> page = new PageInfo(result);
        PagingDTO<T> pagingDTO = new PagingDTO<>();
        pagingDTO.setPage(page.getPageNum());
        pagingDTO.setTotal(page.getPages());
        pagingDTO.setRecords(page.getTotal());
        pagingDTO.setRows(page.getList());
        return pagingDTO;
    }

    /**
     * 准备当前方法实例
     * @param methodName
     * @param service
     * @return
     */
    private Method getCurrMethod(String methodName,Object service){
        Method currM = null;
        for(Method m : service.getClass().getMethods()){
            if(m.getName().equals(methodName)){
                currM = m;
                break;
            }
        }
        return  currM;
    }
}
