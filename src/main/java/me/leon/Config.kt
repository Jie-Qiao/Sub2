package me.leon

import java.io.File


val ROOT = File("sub").absolutePath
val SHARE = "$ROOT\\share"
val SHARE2 = "$ROOT\\share\\private"

//        本地节点池
val POOL = "$ROOT\\pools"
val BIHAI = "$ROOT\\bihai.yaml"

val SPEED_TEST_RESULT = "$ROOT\\speedtest.txt"
val NODE_OK = "$SHARE\\available"
val NODE_SS = "$SHARE\\ss"
val NODE_SS2 = "$SHARE2\\ss2"
val NODE_SSR = "$SHARE\\ssr"
val NODE_SSR2 = "$SHARE2\\ssr2"
val NODE_V2 = "$SHARE\\v2"
val NODE_V22 = "$SHARE2\\v22"
val NODE_TR = "$SHARE\\tr"
val NODE_TR2 = "$SHARE2\\tr2"
val FAIL_IPS = "$ROOT\\socketfail"

