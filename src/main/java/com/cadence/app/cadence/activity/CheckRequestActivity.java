package com.cadence.app.cadence.activity;

import com.cadence.app.cadence.dto.ActivityResponse;
import com.uber.cadence.activity.ActivityMethod;

public interface CheckRequestActivity {

  @ActivityMethod
  ActivityResponse<String> sendTOCheckRequest(String a);
}
