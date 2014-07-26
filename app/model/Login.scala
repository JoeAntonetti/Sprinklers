package model

import reactivemongo.bson._

case class Login(
	id: Option[BSONObjectID],
	username: String,
	password: String
)