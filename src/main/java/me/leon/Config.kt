package me.leon

import java.io.File


val ROOT = File("sub").absolutePath

//        本地节点池
val POOL = "$ROOT\\pools.txt"
val BIHAI = "$ROOT\\bihai.yaml"
val POOL_RAW = "$ROOT\\pools2.txt"
val SPEED_TEST_RESULT = "$ROOT\\speedtest.txt"
val SHARE_NODE = "$ROOT\\share.txt"
val NODE_OK = "$ROOT\\available.txt"
val FAIL_IPS = "$ROOT\\socketfail.txt"

