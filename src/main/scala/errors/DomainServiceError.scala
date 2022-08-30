package errors

sealed trait DomainServiceError {
  val message: String
}

case class MetadataRetrievalError(assetId: String) extends DomainServiceError {
  override val message: String = s"Medatada retrieval for video with assetId: $assetId has failed."
}

case class ChecksumMatchError(assetId: String, checksum1: String, checksum2: String) extends DomainServiceError {
  override val message: String = s"Checksums for $assetId do not match. Compared values: $checksum1 and $checksum2"
}
