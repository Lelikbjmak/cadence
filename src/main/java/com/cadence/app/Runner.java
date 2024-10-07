package com.cadence.app;

import com.cadence.app.cadence.workflow.OsagoContractWorkflow;
import com.epam.edp.spring.boot.cadence.stub.annotation.ClientWorkflow;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

  @ClientWorkflow
  private final OsagoContractWorkflow osagoContractWorkflow;

  @Override
  public void run(String... args) throws Exception {
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    List<Callable<String>> tasks = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      final int finalI = i;
      tasks.add(() -> osagoContractWorkflow.process("String" + finalI));
    }
    executorService.invokeAll(tasks);
  }
}
