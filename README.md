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
2. Open the project in Android Studio
3. Build the project and run it on an Android device.

### Permissions

## The app requires the following permissions:

- CAMERA: To access the camera feed for object detection.
- **INTERNET: For potential future online features.
- **VIBRATE: For vibration feedback when new objects are detected.
- **FLASHLIGHT: To control the device flashlight when in low-light conditions.
- **Make sure to grant these permissions to ensure the app works as expected.

### Usage
1. Object Detection: Start the app and point your camera at objects. The app will announce detected objects via TTS.
2. Flashlight Control: The flashlight will automatically turn on in low-light conditions. You can also manually toggle the flashlight using the button at the bottom-right of the screen.
3. Customizing Voice Settings: Use the sliders at the bottom of the screen to adjust speech rate and pitch for the TTS system.
4. Tutorial Mode: A tutorial will guide new users through the app on the first launch.

### Technologies Used
- **Android (Kotlin)
- **TensorFlow Lite: For object detection.
- **Google Text-to-Speech (TTS): For audio feedback.
- **Camera2 API: For live camera feed and image processing.
- **Vibration API: For tactile notifications.
