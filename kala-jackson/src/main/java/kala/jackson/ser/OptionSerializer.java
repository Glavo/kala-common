package kala.jackson.ser;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.ReferenceTypeSerializer;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.util.NameTransformer;
import kala.control.Option;

public class OptionSerializer extends ReferenceTypeSerializer<Option<?>> {
    private static final long serialVersionUID = 0L;

    public OptionSerializer(ReferenceType fullType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> ser) {
        super(fullType, staticTyping, vts, ser);
    }

    public OptionSerializer(ReferenceTypeSerializer<?> base, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSer, NameTransformer unwrapper, Object suppressableValue, boolean suppressNulls) {
        super(base, property, vts, valueSer, unwrapper, suppressableValue, suppressNulls);
    }

    @Override
    protected ReferenceTypeSerializer<Option<?>> withResolved(BeanProperty prop, TypeSerializer vts, JsonSerializer<?> valueSer, NameTransformer unwrapper) {
        if ((_property == prop)
                && (_valueTypeSerializer == vts) && (_valueSerializer == valueSer)
                && (_unwrapper == unwrapper)) {
            return this;
        }
        return new OptionSerializer(this, prop, vts, valueSer, unwrapper, _suppressableValue, _suppressNulls);
    }

    @Override
    public ReferenceTypeSerializer<Option<?>> withContentInclusion(Object suppressableValue, boolean suppressNulls) {
        return new OptionSerializer(this, _property, _valueTypeSerializer,
                _valueSerializer, _unwrapper,
                suppressableValue, suppressNulls);
    }

    @Override
    protected boolean _isValuePresent(Option<?> value) {
        return value.isDefined();
    }

    @Override
    protected Object _getReferenced(Option<?> value) {
        return value.get();
    }

    @Override
    protected Object _getReferencedIfPresent(Option<?> value) {
        return value.isDefined() ? value.get() : null;
    }
}
