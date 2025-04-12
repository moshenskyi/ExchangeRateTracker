# 💱 Exchange Rate Tracker

An Android app to track and manage your favorite currency exchange rates. The app supports offline mode. All the data the user gets from API are cached in DB. In case of connectivity issues, the user gets cached data.

---

This app uses Open Exchange Rate API to fetch real-time exchange rates. The app has 2 screens:

### Watchlist screen
This screen refreshes data every 5 seconds to reach near-real-time results. The user can remove items from the watchlist here.

![Image alt](https://github.com/moshenskyi/ExchangeRateTracker/raw/main/screenshots/empty_list.png)
![Image alt](https://github.com/moshenskyi/ExchangeRateTracker/raw/main/screenshots/watchlist.png)

### Customization screen
The user can add items to the watchlist or remove them on this screen. Users can navigate there by clicking the FAB button.
![Image alt](https://github.com/moshenskyi/ExchangeRateTracker/raw/main/screenshots/customization_screen.png)
![Image alt](https://github.com/moshenskyi/ExchangeRateTracker/raw/main/screenshots/search.png)

## Architecture & Tech Stack
This app uses Clean Architecture, Jetpack Compose, Kotlin, Hilt, Room, Okhttp logging interceptor, Coroutines, and Flow.

### UseCases
Currently, some use cases are quite minimal and serve primarily as redirections. However, they provide a place where additional logic can be moved in the future. For example, the `WatchlistViewModel` currently handles flow combination. While the ViewModel isn't large at the moment, this orchestration logic could be a better fit within a use case over time.

### Errors
The app follows a layered error-handling approach. In the `data` layer, network errors are parsed and mapped to custom exceptions. The `domain` layer (use cases) then transforms these exceptions into `Resource` types using `Resource.ErrorType`. Finally, the UI layer maps each `ErrorType` to a corresponding string resource for display in UI components.

### Packaging
The project is organized by layers rather than feature-based modules, as it's a relatively simple app with no plans for modularization. However, the dependency inversion principle is still followed, using interfaces to decouple layers where appropriate.


### 🔧 Setup Instructions

1. Clone the repo:
   ```bash
   git clone https://github.com/yourusername/exchange-rate-tracker.git
   ```

2. Open the project in Android Studio

3. Add your API key (if expired) to the `APP_ID` variable in `app/build.gradle.kts`. Here is one more debug key: `15a4c23b8a9c4aa785871de4193b8a05`

4. Run the app on an emulator or device
