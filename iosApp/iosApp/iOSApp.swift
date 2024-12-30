import SwiftUI
import YanYouKotoTomoComposeApp

@main
struct iOSApp: App {
    @ObservedObject var themeChanger = ThemeChangerIOS()

    init()
    {
        let appDeclaration = YanYouKotoTomoComposeApp.IOSAppDeclaration(
            themeChanger: themeChanger
        )

        KoinKt.doInitKoin(appDeclaration: appDeclaration)
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
                .id(themeChanger.key)
        }
    }
}
