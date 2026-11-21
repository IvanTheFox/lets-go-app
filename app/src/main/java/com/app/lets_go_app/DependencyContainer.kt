package com.app.lets_go_app

import com.app.lets_go_app.model.IRepository

class DependencyContainer {
    val repository: IRepository = FakeRepository()
}