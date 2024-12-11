import React from 'react';

interface NumberInputProps {
  label: string;
  name: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  required?: boolean;
}

const NumberInput: React.FC<NumberInputProps> = ({
  label,
  name,
  value,
  onChange,
  required = false,
}) => (
  <div className='p-1'>
    <label className="block text-sm font-medium text-gray-700">{label}</label>
    <input
      type="number"
      name={name}
      value={value}
      onChange={onChange}
      required={required}
      className="mt-1 block w-full rounded-md border border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
    />
  </div>
);

export default NumberInput;
