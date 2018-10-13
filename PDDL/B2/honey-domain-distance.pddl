;; Domain definition
(define (domain honey-domain-distance)
  (:requirements :strips :fluents )
  ;; Predicates: Properties of objects that we are interested in (boolean)
  (:predicates
    (POSITION ?x) ; True if x is a position
    (ARM ?x) ; True if x is an arm
    (OBJECT ?x) ; True if x is an object
    (BEE-HOUSE ?x) ; True if x is a bee-house
    (HONEY-POT ?x) ; True if x is a honey pot
    (COSTUME ?x) ; True if x is a costume
    (person-at ?x) ; True if the person is at x
    (object-at ?x ?y) ; True if x is an object (bee-house, honey-pot or costume), y is a position and x is in y
    (free ?x) ; True if x is an arm and there is NO honey pot in it
    (hold ?x ?y) ; True if x is an arm, y is a honey pot and x holds y
    (wearing ?x) ; True if x is a costume and the person is wearing it
    (empty ?x) ; True if x is a honey pot and it is empty
    (has-honey ?x) ; True if the bee-house x has honey
  )
  
  (:functions 
    (distance ?from ?to )
    (total-cost)
  )

  ;; Actions: Ways of changing the state of the world
  ; The person can move from position x to position y
  ; Parameters:
  ; - x: the position from which the person starts moving
  ; - y: the position to which she moves
  (:action move
    ; WRITE HERE THE CODE FOR THIS ACTION
    :Parameters(?pos1 ?pos2)
    :precondition(and (POSITION ?pos1) (POSITION ?pos2) (person-at ?pos1) (> (distance ?pos1 ?pos2) 0) )
    :effect(and (not (person-at ?pos1)) (person-at ?pos2) (increase (total-cost) (distance ?pos1 ?pos2)) )
  )

  ; The person can take a costume x and put it on if she and the costume are both at position y
  ; Parameters:
  ; - x: the costume
  ; - y: the position where the person will put on the costume
  (:action wear-at
    ; WRITE HERE THE CODE FOR THIS ACTION
    ;Remenber that x is the costume y - the position
    :Parameters(?cost ?pos)
    :precondition(and (OBJECT ?cost) (POSITION ?pos) (COSTUME ?cost) (object-at ?cost ?pos) (person-at ?pos))
    :effect(and (wearing ?cost) (not (object-at ?cost ?pos))) ;object changed position-------------
  )

  ; The person can take off the costume x at position y
  ; Parameters:
  ; - x: the costume
  ; - y: the position where the costume will be taken off
  (:action take-off-at
    ; WRITE HERE THE CODE FOR THIS ACTION
    ;Remenber that x is the costume y - the position
    :Parameters(?cost ?pos)
    :precondition(and (OBJECT ?cost) (POSITION ?pos) (COSTUME ?cost) (not (object-at ?cost ?pos)) (person-at ?pos) )
    :effect(and (not (wearing ?cost)) (object-at ?cost ?pos))
  )

  ; The person can collect all honey from bee house w to honey pot x if 
  ; there is honey in the bee house, the honey pot is empty, 
  ; the person is wearing costume y, has a free arm v, and 
  ; she, the bee house and the honey pot are in the same position z.
  ; - x: the honey pot
  ; - y: the costume
  ; - z: the position where the person collects the honey
  ; - w: the bee house
  ; - v: the arm
  (:action collect
    ; WRITE HERE THE CODE FOR THIS ACTION
    :Parameters(?honey ?cost ?pos ?house ?arm)
    :precondition(and (OBJECT ?cost) (OBJECT ?honey) (OBJECT ?house) (ARM ?arm) (BEE-HOUSE ?house) (POSITION ?pos)(COSTUME ?cost)(HONEY-POT ?honey) (person-at ?pos) (object-at ?house ?pos) (object-at ?honey ?pos) (wearing ?cost) (free ?arm)(empty ?honey)(has-honey ?house))
    :effect(and (not (empty ?honey)) (not (has-honey ?house)) (not (free ?arm))) ; arm is still free ?----------
  )

  ; The person can pick-up honey pot x with free arm y at position z
  ; - x: the honey pot
  ; - y: the arm
  ; - z: the position where the object is picked up
  (:action pick-up
    ; WRITE HERE THE CODE FOR THIS ACTION
    :Parameters(?honey ?arm ?pos)
    :precondition(and (OBJECT ?honey) (HONEY-POT ?honey) (ARM ?arm) (POSITION ?pos) (object-at ?honey ?pos) (person-at ?pos) (free ?arm)  )
    :effect(and (not (free ?arm)) (hold ?arm ?honey) (not (object-at ?honey ?pos) ) ) ; honey pot not in the same position
  )

  ; The person can drop off honey pot x that she holds in arm y at position z
  ; - x: the honey pot
  ; - y: the arm
  ; - z: the position where the object is dropped off
  (:action drop-off
    ; WRITE HERE THE CODE FOR THIS ACTION
    :Parameters(?honey ?arm ?pos)
    :precondition(and (OBJECT ?honey) (HONEY-POT ?honey) (ARM ?arm) (POSITION ?pos) (person-at ?pos) (not (object-at ?honey ?pos) ) (not (free ?arm))  )
    :effect(and  (free ?arm) (not (hold ?arm ?honey)) (object-at ?honey ?pos) )
  )

)
