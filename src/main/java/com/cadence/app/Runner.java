package com.cadence.app;

import com.cadence.app.cadence.workflow.OsagoContractWorkflow;
import com.epam.edp.spring.boot.cadence.stub.annotation.ClientWorkflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

  @ClientWorkflow
  private final OsagoContractWorkflow osagoContractWorkflow;

  @Override
  public void run(String... args) throws Exception {
    String response = osagoContractWorkflow.process("String");
    log.info("Workflow successfully executed: " + response);
  }
}
