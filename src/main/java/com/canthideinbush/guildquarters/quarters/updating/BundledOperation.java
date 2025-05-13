package com.canthideinbush.guildquarters.quarters.updating;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BundledOperation extends UpdateOperation {

    public BundledOperation(List<? extends UpdateOperation> operations) {
        this.operations = operations;
    }

    private final List<? extends UpdateOperation> operations;

    public List<? extends UpdateOperation> getOperations() {
        return operations;
    }

    public static ArrayList<UpdateOperation> getAllBundledOperations(BundledOperation bundledOperation) {
        ArrayList<UpdateOperation> list = new ArrayList<>();
        bundledOperation.getOperations().forEach(
                operation -> {
                    if (operation instanceof BundledOperation) {
                        list.addAll(getAllBundledOperations(bundledOperation));
                    }
                    list.add(operation);
                }
        );
        return list;
    }

    @Override
    public void run() {
        operations.forEach(operation -> run());
    }

    @Override
    public boolean isComplete() {
        return operations.stream().allMatch(UpdateOperation::isComplete);
    }
}
