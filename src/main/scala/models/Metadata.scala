package models

case class Metadata(
   sha1: String,
   sha256: String,
   md5: String,
   crc32: String,
   videoQuality: VideoQuality,
   identifiers: Identifiers
)

case class VideoQuality(frameRate:String, resolution: String, dynamicRange: String)

case class Identifiers(
   productionId: String,
   title: String,
   seriesNumber: Option[Int] = None,
   episodeNumber: Option[Int] = None,
   duration: String
)