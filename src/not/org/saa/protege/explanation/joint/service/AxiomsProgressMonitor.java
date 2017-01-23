package not.org.saa.protege.explanation.joint.service;

import java.util.List;

public interface AxiomsProgressMonitor<E> {

    /**
     * Called by explanation generators that support progress monitors.  This is
     * called when a new explanation is found for an entailment when searching for
     * multiple explanations.
     *
     * @param generator            The explanation generator that found the explanation
     * @param explanation          The explanation that was found
     *                             for the entailment or <code>false</code> if the explanation generator should stop finding explanations
     *                             at the next opportunity.
     * @param allFoundExplanations All of the explanations found so far for the specified entailment
     */
    void foundExplanation(List<E> explanation);

    /**
     * The explanation generator will periodically check to see if it should continue finding explanations by calling
     * this method.
     *
     * @return <code>true</code> if the explanation generator should cancel the explanation finding process or <code>false</code>
     *         if the explanation generator should continue.
     */
    boolean isCancelled();
}