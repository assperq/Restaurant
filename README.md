# Описание проекта: Приложение для ресторанов (Android Kotlin)

## Общая архитектура проекта

Проект реализован с использованием современных Android-технологий и лучших практик разработки:

- Язык: Kotlin
- Архитектура: MVVM (Model-View-ViewModel)
- UI Framework: Jetpack Compose
- Многомодульность: Проект разделен на логические модули
- DI: Koin для dependency injection
- Сетевые запросы: Supabase в качестве backend решения
- Загрузка изображений: Coil

## Модульная структура проекта

Проект разделен на следующие модули:

| Модуль          | Назначение                                                                 |
|-----------------|---------------------------------------------------------------------------|
| `:app`         | Главный модуль приложения, точка входа, также хранит  в себе все koin модули.                                    |
| `:registration`| Аутентификация: вход, регистрация, выход из аккаунта                  |
| `:order`| Заказы блюд ресторана, также включает в себя модуль оплаты                                  |
| `:reservations`| Бронирование столиков             |
| `:profile`| Профиль пользователя: личные данные, история заказов, броней, специфичные функции для администратора                   |
| `:payment`| Мок модуля платежей                          |
| `:statistics`| Статистика ресторана (официантов и блюд за два месяца выбранный и прошлый)                         |

Каждый feature-модуль содержит свою собственную реализацию:

- Data (имплементации репозиториев, источники данных)
- Domain (модели, интерфейсы репозиториев)
- Presentation (Compose UI, ViewModels)

## Ключевые технологии и библиотеки

### Jetpack Compose

Весь UI реализован с помощью декларативного фреймворка Jetpack Compose:

- Использование модификаторов для стилизации
- Кастомные composable компоненты
- Состояние управляется через ViewModel
- Навигация через Compose Navigation

### Koin

DI реализован с помощью Koin:

- Модули организованы по feature-модулям
- Внедрение зависимостей в ViewModels

Пример модуля Koin:

```kotlin
val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    viewModel<AuthViewModel> { AuthViewModel(get(), get()) }
}
```

### Supabase

Для работы с backend используется Supabase:

- Аутентификация (email/password)
- Хранение данных в Postgres
- Хранение файлов (изображений меню)

Пример работы с Supabase:

```kotlin
class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    private val auth = supabaseClient.auth

    override suspend fun signIn(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        contactInfo: String
    ) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
            this.data = buildJsonObject {
                put("full_name", Json.encodeToJsonElement(contactInfo))
            }
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}
```

### Реализация MVVM

Каждый экран следует паттерну MVVM:

- View (Compose):
  - Отображает UI
  - Слушает StateFlow из ViewModel
  - Использует события из ViewModel
- ViewModel:
  - Содержит бизнес-логику
  - Предоставляет состояние через StateFlow
  - Использует репозитории для работы с данными
- Model:
  - Репозитории (интерфейсы и реализации)
  - Data sources (Supabase, локальная БД и т.д.)
  - Domain модели

Пример ViewModel:

```kotlin
class AuthViewModel(
    private val repository: AuthRepository,
    private val profileViewModel: ProfileViewModel
) : ViewModel() {

    private val _state : MutableStateFlow<State> = MutableStateFlow(State.DefaultState)
    val state = _state.asStateFlow()

    sealed class State {
        data object DefaultState : State()
        data class Error(val error : Throwable) : State()
        data class Success(val route : String) : State()
        data object Loading : State()
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        println(error)
        _state.value = State.Error(error)
    }
    private val authScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler)

    fun signIn(email : String, password : String) {
        authScope.launch {
            _state.value = State.Loading
            StringChecker.checkMailString(email)
            StringChecker.checkPassword(password)
            repository.signIn(email, password)
            _state.value = State.Success(AuthRoutes.SignInRoute.route)
            profileViewModel.fetchProfile()
        }
    }

    fun signUp(email: String, password: String, contactInfo : String) {
        authScope.launch {
            _state.value = State.Loading
            if (contactInfo.isEmpty()) {
                throw Exception("Поле с контактной информацией обязательно должно быть заполнено")
            }
            StringChecker.checkMailString(email)
            StringChecker.checkPassword(password)
            repository.signUp(email, password, contactInfo)
            _state.value = State.Success(AuthRoutes.SignUpRoute.route)
            profileViewModel.fetchProfile()
        }
    }

    fun signOut() {
        profileViewModel.clearUser()
        authScope.launch {
            repository.signOut()
        }
    }

    fun clearError() {
        _state.value = State.DefaultState
    }
}
```

#### Особенности реализации

- Аутентификация
  - Интеграция с Supabase Auth
  - Сохранение сессии
- Бронирование столиков
  - Выбор даты
  - Выбор количества гостей
  - История броней
  - Отмена брони
- Заказы
  - Корзина с выбранными блюдами
  - История заказов
  - Статусы заказов
- Кабинет администратора
  - Изменение статуса блюд
  - Завершение броней
  - Текущая схема зала

## Заключение

Данное приложение демонстрирует современный подход к разработке Android-приложений с использованием Kotlin, Jetpack Compose и других современных библиотек. Модульная структура позволяет легко масштабировать проект, добавляя новые функции. Использование Supabase в качестве backend значительно ускорило разработку, предоставив готовые решения для аутентификации, базы данных и хранения файлов.
