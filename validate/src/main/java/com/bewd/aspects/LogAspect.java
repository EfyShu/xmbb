package com.bewd.aspects;

import com.bewd.annotations.Module;
import com.bewd.common.ResultData;
import com.bewd.common.RetCode;
import com.bewd.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.lang.reflect.Method;

/**
 * @Date 2019/4/25 13:45
 * @Created by Efy
 * @Description TODO
 */
@Aspect
@Component
public class LogAspect {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.bewd.annotations.Module)")
    private void moduleLogAspect() {
        log.debug("注册打印日志切面.");
    }

    @Around("moduleLogAspect()")
    private Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        String methodName = point.getSignature().getName();
        Method method = getCurrMethod(methodName,point.getTarget());
        String logPre = getLogPre(method);
        log.info("{} 请求:{}",logPre,point.getArgs()[0]);
        ResultData result = null;
        try{
            Object rawResult = point.proceed();
            if(rawResult instanceof ResultData){
                result = (ResultData) rawResult;
                if(result != null && result.getMessage() != null){
                    result.setMessage(result.getMessage().replaceAll("\\n"," "));
                }
                long time = System.currentTimeMillis() - beginTime;
                result.setCost(time);
                log.info("{} 返回:{}",logPre,result);
            }else{
                log.info("{} 返回:{}",logPre,rawResult);
                return rawResult;
            }
        }catch (Exception e){
            //如果存在事务设置事务回滚
            if(method.isAnnotationPresent(Transactional.class)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            Object returnType = method.getReturnType();
            log.error(logPre+" 执行异常:",e);
            if(returnType instanceof ResultData){
                result = ResultData.failed(RetCode.EXCEPTION);
                long time = System.currentTimeMillis() - beginTime;
                result.setCost(time);
                log.info("{} 返回:{}",logPre,result);
            }else{
                throw e;
            }
        }
        return result;
    }

    /**
     * 拼接模块日志前缀
     * @param currM
     * @return
     */
    private String getLogPre(Method currM){
        Module module = currM.getAnnotation(Module.class);
        String name = StringUtil.isNull(module.value()) ? module.name() : module.value();
        StringBuffer pre = new StringBuffer();
        for(String tag : module.tags()){
            pre.append("【").append(tag).append("】");
        }
        pre.append(name);
        return pre.toString();
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
