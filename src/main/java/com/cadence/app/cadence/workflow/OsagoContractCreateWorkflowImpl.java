package com.cadence.app.cadence.workflow;

import com.cadence.app.service.TOCheckRequestActivityHandler;
import com.epam.edp.tracing.annotation.NewChildSpan;
import com.uber.cadence.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

/**
 * implementation of check workflow. tmp.
 */
@RequiredArgsConstructor
@com.epam.edp.spring.boot.cadence.worker.annotation.Workflow
public class OsagoContractCreateWorkflowImpl implements OsagoContractWorkflow {

  private static final Logger log = Workflow.getLogger(OsagoContractCreateWorkflowImpl.class);

  private final TOCheckRequestActivityHandler toCheckRequestActivityHandler;

  @NewChildSpan(spanName = "cadence_wrokflow")
  public String process(String s1) {
    String res = toCheckRequestActivityHandler.handle();
    return "Workflow completed successfully with response" + res;
  }
}
