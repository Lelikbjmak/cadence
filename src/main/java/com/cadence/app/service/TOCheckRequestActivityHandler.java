package com.cadence.app.service;

import com.cadence.app.cadence.activity.CheckRequestActivity;
import com.epam.edp.spring.boot.cadence.stub.annotation.GlobalActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TOCheckRequestActivityHandler {

  @GlobalActivity
  private final CheckRequestActivity checkRequestActivity;

  public String handle() {
    return checkRequestActivity.sendTOCheckRequest("Request").getResult();
  }
}
