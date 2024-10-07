package com.cadence.app.controller;

import com.cadence.app.cadence.workflow.OsagoContractWorkflow;
import com.epam.edp.spring.boot.cadence.stub.annotation.ClientWorkflow;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workflows")
public class WorkflowController {

  @ClientWorkflow
  private final OsagoContractWorkflow osagoContractWorkflow;

  @SneakyThrows
  @PostMapping
  public String createWorkflow() {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    Callable<String> runnable = () -> {
      String response = osagoContractWorkflow.process("String");
      log.info("Workflow successfully executed: " + response);
      return response;
    };
    List<Callable<String>> tasks = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      tasks.add(runnable);
    }
    executorService.invokeAll(tasks);
    return "Accepted";
  }
}
