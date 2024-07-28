package com.julia.imp.common.filesystem

import platform.Foundation.NSString
import platform.Foundation.stringByAppendingPathComponent

@Suppress("CAST_NEVER_SUCCEEDS")
fun String.appendPathComponent(component: String): String =
    (this as NSString).stringByAppendingPathComponent(component)