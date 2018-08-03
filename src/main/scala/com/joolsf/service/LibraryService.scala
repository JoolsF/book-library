package com.joolsf.service

import com.joolsf.db.LibraryRepository
import com.joolsf.entities.{ Book, BookRequest }

import scala.concurrent.{ ExecutionContext, Future }

class LibraryService(libraryRepository: LibraryRepository) {

  def addBook(bookRequest: BookRequest)(implicit ec: ExecutionContext): Future[Option[Book]] =
    libraryRepository.getBook(bookRequest.title)
      .flatMap {
        case Some(s) =>
          libraryRepository.updateBookNumber(s)
            .flatMap(_ => libraryRepository.getBook(bookRequest.title))
        case None =>
          libraryRepository.addBook(bookRequest.title)
            .flatMap(_ => libraryRepository.getBook(bookRequest.title))
      }

}
