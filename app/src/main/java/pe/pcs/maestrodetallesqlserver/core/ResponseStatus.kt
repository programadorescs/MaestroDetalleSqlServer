package pe.pcs.maestrodetallesqlserver.core

// <T> funciona para cualquier tipo de datos que metamos aqui
sealed class ResponseStatus<T> {
    class Success<T>(var data: T): ResponseStatus<T>()
    class Loading<T>: ResponseStatus<T>()
    class Error<T>(var message: String): ResponseStatus<T>()
}