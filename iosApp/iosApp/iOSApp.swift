import SwiftUI
import YanYouKotoTomoComposeApp

@main
struct iOSApp: App {
    init()
    {
        KoinKt.doInitKoin(appDeclaration: {_ in })
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
