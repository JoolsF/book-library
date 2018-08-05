package com.joolsf

import com.joolsf.entities.LibraryError

import scala.concurrent.Future

package object service {

  // Future result type for a more complex range of error states
  type Result[A] = Future[Either[LibraryError, A]]

  implicit class RichOption[A](o: Option[A]) {
    def toEither(error: LibraryError): Either[LibraryError, A] = o match {
      case Some(v) => Right(v)
      case None => Left(error)
    }
  }

}
