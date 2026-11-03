1. boolean cartPreviewNeedsCoupons = request.getMode() == UnifiedPricingRequest.Mode.CART
   && lines.stream().anyMatch(line -> !line.isSelected() && benefitEligible(line));
   if (identity.couponEnabled() && (cartPreviewNeedsCoupons || !identity.getIntent().isNone())
   && lines.stream().anyMatch(this::benefitEligible)) {
   // SPECIFIED 会补查指定券；未勾选行 AUTO 预估复用这批候选券。
   candidates = candidateLoader.load(identity, now);
   jumps = jumpResolver.forCandidates(candidates, now);
   } 算价代码判断逻辑为什么这么多？为什么不能见名知意？面向对象编程动么？整洁性代码懂么？算价核心代码都检查下