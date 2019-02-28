package husky.sql.calcite.plan.nodes.logical.converter;

import husky.sql.calcite.plan.nodes.HuskyConventions;
import husky.sql.calcite.plan.nodes.logical.HuskyLogicalAgg;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.logical.LogicalAggregate;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.AggregateCall;
import org.apache.calcite.util.ImmutableBitSet;
import org.apache.calcite.util.ImmutableBitSet;

import java.util.List;

public class HuskyLogicalAggConverter extends ConverterRule {

    public static HuskyLogicalAggConverter INSTANCE = new HuskyLogicalAggConverter();

    public HuskyLogicalAggConverter() {
        super(LogicalAggregate.class, Convention.NONE, HuskyConventions.LOGICAL, "HuskyLogicalAggConverter");
    }

    @Override
    public RelNode convert(RelNode rel) {
        LogicalAggregate calc = (LogicalAggregate) rel;
        RelTraitSet traitSet = rel.getTraitSet().replace(HuskyConventions.LOGICAL);
        RelNode newInput = RelOptRule.convert(calc.getInput(), HuskyConventions.LOGICAL);
        boolean indicator = ((LogicalAggregate) rel).indicator;
        ImmutableBitSet groupSet = ((LogicalAggregate) rel).getGroupSet();
        List<ImmutableBitSet> groupSets = ((LogicalAggregate) rel).getGroupSets();
        List<AggregateCall> aggCalls = ((LogicalAggregate) rel).getAggCallList();

        return new HuskyLogicalAgg(rel.getCluster(), traitSet, newInput, indicator, groupSet, groupSets, aggCalls);
    }
}