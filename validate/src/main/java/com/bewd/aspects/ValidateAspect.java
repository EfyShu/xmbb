package com.bewd.aspects;

import com.bewd.annotations.CheckParams;
import com.bewd.annotations.NeedRelease;
import com.bewd.annotations.RuleClass;
import com.bewd.common.ResultData;
import com.bewd.core.ValidateUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2019/4/25 13:45
 * @Created by Efy
 * @Description TODO
 */
@Aspect
@Component
public class ValidateAspect {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.bewd.annotations.CheckParams)")
    private void checkParamsAspect() {
        log.debug("注册检查参数切面.");
    }

    @Around("checkParamsAspect()")
    private ResultData around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object service = point.getTarget();
        String methodName = point.getSignature().getName();
        Method currM = getCurrMethod(methodName, service);
        ValidateUtil valid = init(currM, service);
        ResultData result;
        if (!checkParam(valid, point)) {
            StringBuffer stringBuffer = new StringBuffer();
            int code = 0;
            for (int i = 0; i < valid.getErrorFields().length; i++) {
                ValidateUtil.ErrorBean error = valid.getError(valid.getErrorFields()[i]);
                code = error.getRetCode();
                stringBuffer.append("{").append(error.getErrorMsg()).append("}");
            }
            result = ResultData.failed(code, stringBuffer.toString());
            long time = System.currentTimeMillis() - beginTime;
            result.setCost(time);
            log.debug(time + "ms");
        } else {
            result = (ResultData) point.proceed();
        }
        long time = System.currentTimeMillis() - beginTime;
        result.setCost(time);
        log.debug(time + "ms");
        return result;
    }

    @After("checkParamsAspect()")
    private void after(JoinPoint point) throws IllegalAccessException {
        for (Field f : point.getTarget().getClass().getFields()) {
            if (f.isAnnotationPresent(NeedRelease.class)) {
                f.setAccessible(true);
                if (f.getType().equals(ThreadLocal.class)) {
                    ((ThreadLocal) f.get(point.getTarget())).remove();
                } else {
                    f.set(point.getTarget(), null);
                }
            }
        }
        log.debug("清理资源完成");
    }

    /**
     * 校验方法
     *
     * @param valid
     * @param point
     * @return
     */
    private boolean checkParam(ValidateUtil valid, ProceedingJoinPoint point) {
        log.debug("检查参数...");
        if (valid == null || valid.valid(point.getArgs()[0])) {
            log.debug("参数校验完成");
            return true;
        }
        for (String field : valid.getErrorFields()) {
            ValidateUtil.ErrorBean error = valid.getError(field);
            log.debug("参数校验不通过:" + field + "->" + error.getErrorMsg());
        }
        return false;
    }

    /**
     * 初始化校验工具
     *
     * @param currM
     * @param service
     * @return
     */
    private ValidateUtil init(Method currM, Object service) {
        List<Object> ruleClasses = getRuleClasses(service);
        ValidateUtil valid = ValidateUtil.build(ruleClasses.toArray());
        if (currM == null) {
            return valid;
        }
        if (currM.isAnnotationPresent(CheckParams.class)) {
            CheckParams cp = currM.getAnnotation(CheckParams.class);
            valid.setRequireParam(cp.requireParam());
            valid.setExcludeRules(cp.excludeRules());
            valid.setCheckWithError(cp.checkWithError());
        }
        return valid;
    }

    /**
     * 准备当前方法实例
     *
     * @param methodName
     * @param service
     * @return
     */
    private Method getCurrMethod(String methodName, Object service) {
        Method currM = null;
        for (Method m : service.getClass().getMethods()) {
            if (m.getName().equals(methodName)) {
                currM = m;
                break;
            }
        }
        return currM;
    }

    /**
     * 准备规则类
     *
     * @param service
     * @return
     */
    private List<Object> getRuleClasses(Object service) {
        List<Object> ruleClasses = new ArrayList<>();
        ruleClasses.add(service);
        Field[] allFields = service.getClass().getDeclaredFields();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(RuleClass.class)) {
                try {
                    //关闭可访问校验
                    field.setAccessible(true);
                    ruleClasses.add(field.get(service));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return ruleClasses;
    }
}
