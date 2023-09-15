# Android Project Structure

- This Android application follows a common [architectural](https://developer.android.com/topic/architecture) pattern known as MVVM (Model-View-ViewModel) with a combination of [Composables](https://developer.android.com/jetpack/compose) for UI, [Parcelable](https://developer.android.com/reference/android/os/Parcelable) for data serialization, and [Firebase Firestore](https://firebase.google.com/docs/firestore/data-model) for database connectivity ([Adding, updating](https://firebase.google.com/docs/firestore/manage-data/add-data) and [deleting](https://firebase.google.com/docs/firestore/manage-data/delete-data) data, Reading data [Queries](https://firebase.google.com/docs/firestore/query-data/get-data) and [observable queries](https://firebase.google.com/docs/firestore/query-data/listen)).


## UI Components (Activity and Composables):

- [Activity](https://developer.android.com/reference/android/app/Activity): __Activities__ serve as the entry point to the application and manage the overall user interface(UI). They can host __Composables__, which are a part of Jetpack Compose, a modern Android UI toolkit.

- __Composables__: __Composables__ are used to define the UI elements and their behavior in a declarative way. They replace the traditional XML layouts in Android and allow to create the UI using Kotlin code.

- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): The __ViewModel__ is a part of the MVVM architecture. It's responsible for holding and managing the UI-related data. __ViewModel__ instances survive configuration changes (like screen rotations) and ensure that data is available to the UI components. In this case, the __ViewModel__ likely communicates with the Firebase Firestore database to fetch, update, or delete data asynchronously. It might expose LiveData or State in __Composables__ to observe and display data changes in the UI.

## Domain:

- The [domain](https://developer.android.com/topic/architecture/domain-layer) layer typically contains the business logic, data models, and rules for this application. These models represent the core data structures and concepts in this app. They are often plain Kotlin data classes.

- The __domain__ layer enforces rules and validations, ensuring the integrity and consistency of data before interacting with the database.

## Adapters:

- __Adapters__ are responsible for connecting this application to external data sources or services, such as [Firebase Firestore](https://firebase.google.com/docs/firestore/data-model) in this case. They contain asynchronous functions for reading and writing data.

- Theres also a need of transform data between the domain models and Firestore documents.

![Overview](mad-arch-overview.png)

## Summary

- This organization follows a modern Android architecture (MVVM) that separates different layers: UI components for rendering the user interface, ViewModels for managing UI-related data and interactions, the domain layer for core business logic and data models, and adapters for handling data communication with Firebase Firestore. Parcelable is used for efficient data serialization, especially when passing data between activities or fragments. This organization helps maintain a clear separation of concerns and makes the code more modular and testable.
