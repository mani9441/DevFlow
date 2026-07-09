# DevFlow

DevFlow is a premium, feature-rich Android developer workspace application designed to streamline software project management, team collaboration, and DevOps monitoring in a unified, modern interface. Built with Jetpack Compose, clean architecture principles, and material design elements, DevFlow provides developers and lead engineers with a comprehensive toolkit to manage their workflow from sprint planning to build deployment.

---

## 🚀 Features

### 📅 Agile & Project Management
- **Sprints**: Manage development iterations with target dates and visual progress tracking.
- **Development Tasks**: Track task lifecycles (Backlog, In Progress, In Review, Done) with priority levels.
- **User Stories**: Write and manage user stories with acceptance criteria, priority, and epic alignment.
- **Issue Tracker**: Track bugs, system bottlenecks, and tasks with priority levels and custom status tracking.

### 🤝 Team Collaboration
- **Team Chat**: Instant developer messaging with team members.
- **Meetings**: Schedule and log standups, planning sessions, and retrospectives with dedicated sections for yesterday's work, today's plan, and blockers.

### 📝 Personal Workspace
- **Personal Todos**: A lightweight todo tracker for everyday dev tasks.
- **Project Notes**: Rich text-style project notes and developer journals.
- **Deadlines**: Set notifications and track critical project milestones and launch dates.

### ⚙️ DevOps Integration
- **CI/CD Builds Monitoring**: Real-time monitoring of GitHub Actions workflow runs, status indications, branch info, build logs, and conclusion details.

---

## 🛠 Tech Stack

- **UI Framework**: [Jetpack Compose](https://redirection.to.android.com/jetpack/compose) (Declarative UI)
- **Programming Language**: [Kotlin](https://kotlinlang.org/)
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) (Dagger Hilt)
- **Local Database**: [Room Database](https://developer.android.com/training/data-storage/room) (SQLite ORM)
- **Architecture**: Clean Architecture (Presentation, Domain, Data layer separations)
- **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/guide/navigation/navigation-principles)
- **Asynchronous Flow**: Kotlin Coroutines & Flow

---

## 📁 Architecture Overview

DevFlow follows a feature-grouped Clean Architecture folder structure:
```
com.devflow.app/
│
├── core/                  # Core modules (design system, common, themes)
│   └── designsystem/      # Reusable UI components (AppTopBar, AppCard, AppButton, AppTextField, etc.)
│
├── data/                  # App database converters, migrations, and shared Room config
│
├── navigation/            # Navigation routing and global NavigationGraph setup
│
└── features/              # Feature modules containing independent Data, Domain, and Presentation layers
    ├── dashboard/         # Combined project overview and key statistics
    ├── sprint/            # Iteration planning
    ├── tasks/             # Project tasks lifecycle
    ├── stories/           # Product backlog item tracking
    ├── issues/            # Bug/issue resolution tracking
    ├── communication/     # Team members chat
    ├── meetings/          # Meeting logs
    ├── notes/             # Dev journals
    ├── todo/              # Developer tasks list
    ├── deadlines/         # Countdown metrics
    └── monitoring/        # GitHub Actions workflow monitor
```

---

## ⚙️ Setup & Installation

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 21
- Android SDK 29+

### Building the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/mani9441/DevFlow.git
   cd DevFlow
   ```
2. Open the project in Android Studio.
3. Build the application using Gradle:
   ```bash
   ./gradlew assembleDebug
   ```

### Running Tests
To verify correct behavior, execute the unit test suites:
```bash
./gradlew test
```
