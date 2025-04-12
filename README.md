# 💱 Exchange Rate Tracker

An Android app to track and manage your favorite currency exchange rates. The app supports offline mode. All the data the user gets from API are cached in DB. In case of connectivity issues, the user gets cached data.

---

This app uses Open Exchange Rate API to fetch real-time exchange rates. The app has 2 screens:

### Watchlist screen
This screen refreshes data every 5 seconds to reach near-real-time results. The user can remove items from the watchlist here.

### Customization screen
The user can add items to the watchlist or remove them on this screen. Users can navigate there by clicking the FAB button.

## Architecture & Tech Stack
This app uses Clean Architecture, Jetpack Compose, Kotlin, Hilt, Room, Okhttp logging interceptor, Coroutines, and Flow.

### UseCases
Right now, some use cases are very small and used for redirecting, but in the future, some of the logic might migrate there. For example, `WatchlistViewModel` combines flows, and while this ViewModel is not big, it's not a very big issue, but this orchestrational logic might look better in use case.

### Errors
The error-handling approach is  the following. On the data layer, the app parses network errors and maps them into custom exceptions. On the domain layer, use cases transform exceptions to `Resource`, based on `Resource.ErrorType` mappers on UI layer map it to appropriate `String` resource that should be shown in UI components.

### Packaging
The packages are divided by layers since it's a simple app, and I don't plan to move it to separate feature modules. Nevertheless, I follow the dependency rule and invert dependencies with interfaces where needed.


### 🔧 Setup Instructions

1. Clone the repo:
   ```bash
   git clone https://github.com/yourusername/exchange-rate-tracker.git
   ```

2. Open the project in Android Studio

3. Add your API key (if expired) to the `APP_ID` variable in `app/build.gradle.kts`. Here is one more debug key: `15a4c23b8a9c4aa785871de4193b8a05`

4. Run the app on an emulator or device
