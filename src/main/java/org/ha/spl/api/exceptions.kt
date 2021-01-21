package org.ha.spl.api

// Hospital
class WardExistException : Exception()

// Ward
class BedExistException : Exception()
class BedOccupiedException : Exception()
class BedNotFoundException : Exception()

// Patient
class PatientCheckedInException : Exception()
class PatientNotCheckedInException : Exception()
class PatientNotFoundException : Exception()