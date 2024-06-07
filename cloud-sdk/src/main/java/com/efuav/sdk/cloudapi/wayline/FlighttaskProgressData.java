package com.efuav.sdk.cloudapi.wayline;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
public class FlighttaskProgressData {

    /**
     * 执行步骤
     */
    private ExecutionStepEnum currentStep;

    /**
     * 进度值
     */
    private Integer percent;

    public FlighttaskProgressData() {
    }

    @Override
    public String toString() {
        return "FlighttaskProgressData{" +
                "currentStep=" + currentStep +
                ", percent=" + percent +
                '}';
    }

    public ExecutionStepEnum getCurrentStep() {
        return currentStep;
    }

    public FlighttaskProgressData setCurrentStep(ExecutionStepEnum currentStep) {
        this.currentStep = currentStep;
        return this;
    }

    public Integer getPercent() {
        return percent;
    }

    public FlighttaskProgressData setPercent(Integer percent) {
        this.percent = percent;
        return this;
    }
}
