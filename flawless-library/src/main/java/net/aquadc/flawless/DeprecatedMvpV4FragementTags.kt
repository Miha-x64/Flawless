package net.aquadc.flawless.tag

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("SupportFragPresenterTag<ARG, RET, PRESENTER>", "net.aquadc.flawless.tag.SupportFragPresenterTag"))
typealias V4FragPresenterTag<ARG, RET, PRESENTER> = SupportFragPresenterTag<ARG, RET, PRESENTER>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("ConsumerSupportFragPresenterTag<ARG, PRESENTER>", "net.aquadc.flawless.tag.ConsumerSupportFragPresenterTag"))
typealias ConsumerV4FragPresenterTag<ARG, PRESENTER> = ConsumerSupportFragPresenterTag<ARG, PRESENTER>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("SupplierSupportFragPresenterTag<RET, PRESENTER>", "net.aquadc.flawless.tag.SupplierSupportFragPresenterTag"))
typealias SupplierV4FragPresenterTag<RET, PRESENTER> = SupplierSupportFragPresenterTag<RET, PRESENTER>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("ActionSupportFragPresenterTag<PRESENTER>", "net.aquadc.flawless.tag.ActionSupportFragPresenterTag"))
typealias ActionV4FragPresenterTag<PRESENTER> = ActionSupportFragPresenterTag<PRESENTER>
