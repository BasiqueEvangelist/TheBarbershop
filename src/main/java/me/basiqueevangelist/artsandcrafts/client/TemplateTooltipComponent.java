package me.basiqueevangelist.artsandcrafts.client;

import io.wispforest.owo.ui.base.BaseOwoTooltipComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;

public class TemplateTooltipComponent extends BaseOwoTooltipComponent<FlowLayout> {
    public TemplateTooltipComponent(ClientHaircutStore.Entry entry) {
        super(() -> {
            var flow = Containers.verticalFlow(Sizing.content(), Sizing.content());

            flow.child(Containers.verticalFlow(Sizing.fixed(100), Sizing.content())
                .child(entry.texture().toComponent()
                    .preserveAspectRatio(true)
                    .horizontalSizing(Sizing.fill(100)))
                .margins(Insets.of(5)));

            return flow;
        });
    }
}
