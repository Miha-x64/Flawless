package net.aquadc.flawless.implementMe

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("SupportFragPresenter<ARG, RET, STATE>", "net.aquadc.flawless.implementMe.SupportFragPresenter"))
typealias V4FragPresenter<ARG, RET, STATE> = SupportFragPresenter<ARG, RET, STATE>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("ConsumerSupportFragPresenter<ARG, STATE>", "net.aquadc.flawless.implementMe.ConsumerSupportFragPresenter"))
typealias ConsumerV4FragPresenter<ARG, STATE> = ConsumerSupportFragPresenter<ARG, STATE>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("SupplierSupportFragPresenter<RET, STATE>", "net.aquadc.flawless.implementMe.SupplierSupportFragPresenter"))
typealias SupplierV4FragPresenter<RET, STATE> = SupplierSupportFragPresenter<RET, STATE>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("ActionSupportFragPresenter<STATE>", "net.aquadc.flawless.implementMe.ActionSupportFragPresenter"))
typealias ActionV4FragPresenter<STATE> = ActionSupportFragPresenter<STATE>


@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("StatelessSupportFragPresenter<ARG, RET>", "net.aquadc.flawless.implementMe.StatelessSupportFragPresenter"))
typealias StatelessV4FragPresenter<ARG, RET> = StatelessSupportFragPresenter<ARG, RET>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("StatelessConsumerSupportFragPresenter<ARG>", "net.aquadc.flawless.implementMe.StatelessConsumerSupportFragPresenter"))
typealias StatelessConsumerV4FragPresenter<ARG> = StatelessConsumerSupportFragPresenter<ARG>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("StatelessSupplierSupportFragPresenter<RET>", "net.aquadc.flawless.implementMe.StatelessSupplierSupportFragPresenter"))
typealias StatelessSupplierV4FragPresenter<RET> = StatelessSupplierSupportFragPresenter<RET>

@Deprecated(message = "renamed", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("StatelessActionSupportFragPresenter", "net.aquadc.flawless.implementMe.StatelessActionSupportFragPresenter"))
typealias StatelessActionV4FragPresenter = StatelessActionSupportFragPresenter
