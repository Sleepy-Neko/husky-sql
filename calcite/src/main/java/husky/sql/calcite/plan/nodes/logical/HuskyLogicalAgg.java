package husky.sql.calcite.plan.nodes.logical;

import husky.sql.calcite.plan.nodes.HuskyRelNode;
import org.apache.calcite.plan.*;
import org.apache.calcite.rel.RelInput;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelShuttle;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.AggregateCall;
import org.apache.calcite.util.ImmutableBitSet;


import java.util.List;


public final class HuskyLogicalAgg extends Aggregate implements HuskyLogicalRel {

    public HuskyLogicalAgg (
            RelOptCluster cluster,
            RelTraitSet traitSet,
            RelNode child,
            boolean indicator,
            ImmutableBitSet groupSet,
            List<ImmutableBitSet> groupSets,
            List<AggregateCall> aggCalls) {
        super(cluster, traitSet, child, indicator, groupSet, groupSets, aggCalls);
    }

    @Deprecated // to be removed before 2.0
    public HuskyLogicalAgg(
            RelOptCluster cluster,
            RelNode child,
            boolean indicator,
            ImmutableBitSet groupSet,
            List<ImmutableBitSet> groupSets,
            List<AggregateCall> aggCalls) {
        this(cluster, cluster.traitSetOf(Convention.NONE), child, indicator,
                groupSet, groupSets, aggCalls);
    }

    /**
     * Creates a LogicalAggregate by parsing serialized output.
     */
    public HuskyLogicalAgg(RelInput input) {
        super(input);
    }

    /** Creates a LogicalAggregate. */
    public static HuskyLogicalAgg create(final RelNode input,
                                          ImmutableBitSet groupSet, List<ImmutableBitSet> groupSets,
                                          List<AggregateCall> aggCalls) {
        return create_(input, false, groupSet, groupSets, aggCalls);
    }

    @Deprecated // to be removed before 2.0
    public static HuskyLogicalAgg create(final RelNode input,
                                          boolean indicator,
                                          ImmutableBitSet groupSet,
                                          List<ImmutableBitSet> groupSets,
                                          List<AggregateCall> aggCalls) {
        return create_(input, indicator, groupSet, groupSets, aggCalls);
    }

    private static HuskyLogicalAgg create_(final RelNode input,
                                            boolean indicator,
                                            ImmutableBitSet groupSet,
                                            List<ImmutableBitSet> groupSets,
                                            List<AggregateCall> aggCalls) {
        final RelOptCluster cluster = input.getCluster();
        final RelTraitSet traitSet = cluster.traitSetOf(Convention.NONE);
        return new HuskyLogicalAgg(cluster, traitSet, input, indicator, groupSet,
                groupSets, aggCalls);
    }

    //~ Methods ----------------------------------------------------------------

    @Override public HuskyLogicalAgg copy(RelTraitSet traitSet, RelNode input,
                                           boolean indicator, ImmutableBitSet groupSet,
                                           List<ImmutableBitSet> groupSets, List<AggregateCall> aggCalls) {
        assert traitSet.containsIfApplicable(Convention.NONE);
        return new HuskyLogicalAgg(getCluster(), traitSet, input, indicator,
                groupSet, groupSets, aggCalls);
    }

    @Override public RelNode accept(RelShuttle shuttle) {
        return shuttle.visit(this);
    }
}