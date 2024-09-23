package com.cadence.app.cadence.domain;

import com.cadence.app.cadence.domain.enumeration.ActivityType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity_context")
public class ActivityContextEntity {

  @Id
  @Column(nullable = false)
  private String activityId;

  @Column(nullable = false)
  private String token;

  @Column(nullable = false)
  private LocalDateTime start;

  @Column(nullable = false)
  private String activityName;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private ActivityType activityType;

}
