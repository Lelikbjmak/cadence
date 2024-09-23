package com.cadence.app.cadence.workflow;

import com.uber.cadence.workflow.WorkflowMethod;

public interface OsagoContractWorkflow {

  @WorkflowMethod(
      name = "createInsuranceContract"
  )
  String process(String s1);
}
