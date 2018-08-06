package com.joolsf.service

import java.time.LocalDate

import com.joolsf.db.LibraryRepository
import com.joolsf.entities._

import scala.concurrent.{ ExecutionContext, Future }

//TODO use Cats EitherT / OptionT rather Future[Either[....]] etc
class LibraryService(libraryRepository: LibraryRepository) {

  def addBook(bookRequest: BookRequest)(implicit ec: ExecutionContext): Future[Option[Book]] =
    libraryRepository.getBook(bookRequest.title).flatMap {
      case Some(s) =>
        libraryRepository.incrementBookNumber(s)
          .flatMap(_ => libraryRepository.getBook(bookRequest.title))
      case None =>
        libraryRepository.addBook(bookRequest.title)
          .flatMap(_ => libraryRepository.getBook(bookRequest.title))
    }

  def getEmployeesLoans(employeeId: Int)(implicit ec: ExecutionContext): Result[List[Loan]] =
    for {
      employeeExists <- libraryRepository.getEmployee(employeeId)
      loan <- libraryRepository.getLoansByEmployeeId(employeeId)
      res <- (employeeExists, loan) match {
        case (None, _) => Future.successful(Left(NotFoundError("")))
        case (Some(_), res) => Future.successful(Right(res))
      }
    } yield res

  def addEmployee(employeeRequest: EmployeeRequest)(implicit ec: ExecutionContext): Future[Option[Int]] =
    libraryRepository.getEmployee(employeeRequest.name).flatMap {
      case Some(_) => Future.successful(None)
      case None => libraryRepository.addEmployee(employeeRequest.name).map(Some(_))
    }

  def addLoan(loanRequest: LoanRequest)(implicit ec: ExecutionContext): Result[LocalDate] = {
    val (from, to) = getLoanDates(28)

    def updateLibrary(book: Book, employee: Employee): Future[LocalDate] = for {
      _ <- libraryRepository.decrementBookNumber(book)
      loan <- libraryRepository.addLoan(book, employee, from, to)
    } yield to

    for {
      book <- libraryRepository.getBook(loanRequest.bookId)
      employee <- libraryRepository.getEmployee(loanRequest.employeeId)
      res <- (book, employee, book.exists(_.numberOfCopies > 0)) match {
        case (Some(b), Some(e), true) =>
          updateLibrary(b, e).map(Right(_))
        case (Some(b), Some(e), false) =>
          Future.successful(Left(BookUnavailable("no copies of book left to loan")))
        case r @ ((None, _, _) | (_, None, _)) =>
          Future.successful(Left(NotFoundError(s"employee exists ${employee.isDefined} book exists ${book.isDefined}")))
        case (_, _, _) =>
          Future.successful(Left(GenericError("")))
      }
    } yield res
  }

  private def getLoanDates(lengthOfLoanInDays: Int): (LocalDate, LocalDate) = {
    val today = LocalDate.now()
    (today, today.plusDays(lengthOfLoanInDays))
  }

}