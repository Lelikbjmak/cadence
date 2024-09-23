package com.cadence.app.config;

import com.cadence.app.cadence.workflow.OsagoContractWorkflow;
import com.epam.edp.cadence.common.client.completion.EnableActivityCompletion;
import com.epam.edp.spring.boot.cadence.EnableCadence;
import com.epam.edp.spring.boot.cadence.worker.context.DefaultWorkerContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableCadence(basePackageClasses = {
    OsagoContractWorkflow.class,
})
@EnableActivityCompletion
@Import({DefaultWorkerContext.class})
public class CadenceConfig {

}
