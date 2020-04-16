package com.proofpoint.wikisystem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class Component implements Operations {
    protected User owner;
    protected Map<AccessType, List<Collaborator>> accessMap;
}
