import SwiftUI
import YanYouKotoTomoComposeApp


class ThemeChangerIOS: ThemeChanger, ObservableObject {
    @Published var key = UUID()

    func applyTheme() {
        key = UUID()
    }

    func activateIcon(icon: AppIcon) {
        Task {
            switch icon {
                case AppIcon.clip :
                    await UIApplication.shared.setAlternateIconName("IconClip"){ error in
                        if let error = error {
                            print(error.localizedDescription)
                        } else {
                            print("Success!")
                        }
                    }
                case AppIcon.verticalhalf :
                    await UIApplication.shared.setAlternateIconName("IconVerticalHalf"){ error in
                        if let error = error {
                            print(error.localizedDescription)
                        } else {
                            print("Success!")
                        }
                    }
                default:
                    await UIApplication.shared.setAlternateIconName(nil){ error in
                        if let error = error {
                            print(error.localizedDescription)
                        } else {
                            print("Success!")
                        }
                    }
            }
        }
    }
}

