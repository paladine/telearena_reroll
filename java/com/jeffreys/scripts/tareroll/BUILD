package(default_visibility = [
    "//javatests/com/jeffreys/scripts/tareroll:__subpackages__"
])

proto_library(
    name = "reroll_proto",
    srcs = ["reroll.proto"],
)

java_proto_library(
    name = "java_reroll_proto",
    deps = [":reroll_proto"],
)

java_library(
    name = "lib",
    srcs = glob(["*.java"]),
    deps = [
        ":java_reroll_proto",
        "//java/com/jeffreys/common/proto",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_protobuf_protobuf_java",
    ],
)

java_binary(
    name = "TAReroll",
    main_class = "com.jeffreys.scripts.tareroll.TAReroll",
    runtime_deps = [":lib"],
)
