package com.joolsf.entities

sealed trait LibraryError {
  def code: Int = 0

  def message: String
}

case class GenericError(message: String) extends LibraryError

case class NotFoundError(message: String) extends LibraryError

case class BookUnavailable(message: String) extends LibraryError
