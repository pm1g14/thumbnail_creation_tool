package errors

sealed abstract class ValidationErrorCode(errorCode: String, errorDescription: String) {
  val code: String = errorCode
  val description: String = errorDescription
}

sealed trait ValidationError {
  val message: String
  val errorCode: ValidationErrorCode
}

case object InvalidDurationFormat extends ValidationErrorCode(
  "invalid",
  "The given duration value is not formatted properly, valid format: HH:mm:ss"
)
case object PathNotFound extends ValidationErrorCode(
  "not-found",
  "The given path to the video file was not found"
)
