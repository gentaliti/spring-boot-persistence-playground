# Item 1: How to Effectively Shape the @OneToMany Association
 
### Notes
- Always cascade from parent to children
- Don't forget to add mappedBy on parentSide
- Don't forget to add orphanRemoval = true. To delete not referenced children
- Keep Both Sides of the Association in Sync
- Override equals and hashcode
- Use fetchType=Lazy on both sides of association
- Pay attention of how toString is overridden. It shouldn't does not involve lazy children
     
