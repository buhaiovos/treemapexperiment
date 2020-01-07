package ua.buhaiovos.sillygames;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.TreeMap;

public class TreeMapPrinter {
    @SneakyThrows
    public <K, V> String treeOfColors(TreeMap<K, V> map) {
        final String[][] array = prepareMatrix(map);
        Object root = extractRootNode(map);
        printNodeToMatrixWithPosition(root, array, new Position(0, map.size()));
        return printArray(array);
    }

    private <K, V> String[][] prepareMatrix(TreeMap<K, V> map) {
        final int size = map.size();
        return new String[size * 2][size * 2 + 2];
    }

    @SneakyThrows
    private <K, V> Object extractRootNode(TreeMap<K, V> map) {
        var aClass = map.getClass();
        Field rootField = aClass.getDeclaredField("root");
        rootField.setAccessible(true);
        return rootField.get(map);
    }

    @SneakyThrows
    private void printNodeToMatrixWithPosition(Object node, String[][] matrix, Position position) {
        Object leftNode = getObjectField(node, "left");
        Object rightNode = getObjectField(node, "right");

        // check if we need to place child nodes with larger distance between each other
        int columnOffset = 2;
        if (leftNode != null && rightNode != null) {
            Object rightGrandChild = getObjectField(leftNode, "right");
            Object leftGrandChild = getObjectField(rightNode, "left");
            if (rightGrandChild != null && leftGrandChild != null) {
                columnOffset = 3;
            }
        }

        //if left present
        //go left
        if (leftNode != null) {
            printNodeToMatrixWithPosition(
                    leftNode,
                    matrix,
                    new Position(position.row + 2, position.column - columnOffset));
        }
        //if right present
        //go right
        if (rightNode != null) {
            printNodeToMatrixWithPosition(
                    rightNode,
                    matrix,
                    new Position(position.row + 2, position.column + columnOffset));
        }

        //print current node
        boolean color = getBooleanField(node, "color");
        matrix[position.row][position.column] = color ? "B" : "R";
        if (leftNode != null) matrix[position.row + 1][position.column - 1] = "/";
        if (rightNode != null) matrix[position.row + 1][position.column + 1] = "\\";
    }

    private Object getObjectField(Object node, String right) throws NoSuchFieldException, IllegalAccessException {
        Field rightField = node.getClass().getDeclaredField(right);
        rightField.setAccessible(true);
        return rightField.get(node);
    }

    private boolean getBooleanField(Object node, String name) throws NoSuchFieldException, IllegalAccessException {
        Field colorField = node.getClass().getDeclaredField(name);
        colorField.setAccessible(true);
        return (boolean) colorField.get(node);
    }

    private String printArray(String[][] array) {
        var matrixStringBuilder = new StringBuilder();
        for (String[] row : array) {
            var rowStringBuilder = new StringBuilder();
            for (String element : row) {
                rowStringBuilder.append(element == null ? " " : element);
            }
            var printedRow = rowStringBuilder.toString();
            if (!printedRow.isBlank()) {
                matrixStringBuilder.append(printedRow);
                matrixStringBuilder.append("\n");
            }
        }
        return matrixStringBuilder.toString();
    }

    @AllArgsConstructor
    private static class Position {
        final int row;
        final int column;
    }
}

