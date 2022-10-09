//
//  ContentView.swift
//  iosApp
//
//  Created by Debojyoti Singha on 09/10/22.
//

import SwiftUI
import gitmanagersharedmodule

struct ContentView: View {
    var body: some View {
        Text(Greeting().greeting())
        .padding()
    }
}
