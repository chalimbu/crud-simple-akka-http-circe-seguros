package com.main

import com.configurations.ApiErrorConfig

object PolicyValidator {
  def validate(policy: Policy): Option[ApiErrorConfig] = {
    if (policy.owner.isEmpty || policy.creditor.isEmpty) Some(ApiErrorConfig.missingFields)
    else None
  }
}
