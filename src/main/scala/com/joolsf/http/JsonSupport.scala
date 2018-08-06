package com.joolsf.http

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.{ Encoder, Json }

trait JsonSupport extends FailFastCirceSupport {

  private val localDateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE

  implicit val localDateEncoder: Encoder[LocalDate] =
    Encoder.instance(i => Json.fromString(localDateFormatter.format(i)))

}

