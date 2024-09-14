# Visual Assistance App (VAA)

**Visual Assistance App (VAA)** is an Android application designed to assist visually impaired users by detecting objects through a camera feed and providing real-time audio feedback. The app uses a TensorFlow Lite model for offline object detection, integrates Text-to-Speech (TTS) for audible feedback, and offers several accessibility features like vibration notifications and flashlight controls in low-light conditions.

## Features

- **Real-time Object Detection**: Utilizes TensorFlow Lite to detect objects through the camera.
- **Text-to-Speech (TTS) Feedback**: Provides audio feedback for detected objects using Google's TTS.
- **Vibration Feedback**: Notifies users via vibration when new objects are detected.
- **Flashlight Control**: Automatically toggles the flashlight based on ambient light conditions, or can be manually controlled.
- **Voice Settings**: Users can customize TTS settings like speech rate and pitch.
- **Tutorial Mode**: First-time users are guided through the basic app features via an on-screen tutorial.
- **Offline Mode (Coming Soon)**: Allows the app to work without internet access.
- **Error Handling and Alerts (Coming Soon)**: Provides user-friendly error messages and sound alerts.
- **Multiple Language Support (Coming Soon)**: Adds support for multiple languages in TTS and UI.

## Getting Started

### Prerequisites

- Android device running Android 7.0 (API 24) or higher.
- Basic knowledge of Android Studio.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-github-username/vaa.git
