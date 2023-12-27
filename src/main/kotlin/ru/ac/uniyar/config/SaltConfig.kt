package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.nonEmptyString

class SaltConfig(
    val salt: String
) {
    companion object {
        val saltLens = EnvironmentKey.nonEmptyString().required("salt")
        val defaultEnv: Environment = Environment.defaults(
            saltLens of "fbjhavfdhjvhjfahjdfbajbfhdahfhadhjfvhjdavfhjadvfhjavdhfahjvdfhjavjhfvadhjfvhjavfdhjvajfhv" +
                "fdlkjafknadjgnjkadbgjkdabgjdbajfbadjfbajdbfjbdakfjbadjkfnajkdbfnajfdbnajknfkjDFNAMANDFJNDJFNAJF",
        )
        fun fromEnvironmentSalt(environment: Environment): SaltConfig =
            SaltConfig(
                saltLens(environment)
            )
    }
}
