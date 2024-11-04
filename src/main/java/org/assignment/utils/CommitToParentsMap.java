package org.assignment.utils;

import java.util.HashMap;
import java.util.List;

/**
 * Map each commit to its real parent which is in case of a merge the first parent of the parents list
 */
public class CommitToParentsMap extends HashMap<String, List<String>> {

}
