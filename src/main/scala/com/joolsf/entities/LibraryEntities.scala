package com.joolsf.entities

import java.time.LocalDate

case class Employee(id: Long, name: String)

case class Book(id: Long, title: String, numberOfCopies: Int)

case class Loan(id: Long, book: Book, employee: Employee, loanDate: LocalDate, returnDate: LocalDate)

//HTTP
case class EmployeeRequest(name: String)

case class BookRequest(title: String)

case class LoanRequest(bookId: Long, employeeId: Long)